import cv2
import torch
import numpy as np
from models.superpoint import SuperPoint
from models.superglue import SuperGlue

# ==========================
# 1. 模型配置
# ==========================
device = 'cuda' if torch.cuda.is_available() else 'cpu'
print(f"Using device: {device}")

superpoint_config = {
    'descriptor_dim': 256,
    'nms_radius': 4,
    'keypoint_threshold': 0.005,
    'max_keypoints': 1024,
}

superglue_config = {
    'weights': 'indoor',
    'sinkhorn_iterations': 20,
    'match_threshold': 0.2,
}

superpoint = SuperPoint(superpoint_config).to(device)
superglue = SuperGlue(superglue_config).to(device)

# ==========================
# 2. 读取图片
# ==========================
template_path = "template2.png"
scene_path = "test_micro_1.png"

img1 = cv2.imread(template_path, cv2.IMREAD_GRAYSCALE)
img2 = cv2.imread(scene_path, cv2.IMREAD_GRAYSCALE)
cv2.imshow("Template", img1)
cv2.imshow("Scene", img2)

# 转成Tensor并归一化
def preprocess(img):
    img = torch.from_numpy(img / 255.).float()[None, None].to(device)
    return img


img1_t = preprocess(img1)
img2_t = preprocess(img2)

# ==========================
# 3. SuperPoint提取特征
# ==========================
with torch.no_grad():
    pred1 = superpoint({'image': img1_t})
    pred2 = superpoint({'image': img2_t})

# 检查关键点数量
if pred1['keypoints'][0].shape[0] == 0 or pred2['keypoints'][0].shape[0] == 0:
    raise RuntimeError("未检测到关键点，请检查输入图片。")

# ==========================
# 4. SuperGlue匹配
# ==========================
data = {
    'image0': img1_t,
    'image1': img2_t,
    'keypoints0': pred1['keypoints'][0].unsqueeze(0),
    'scores0': pred1['scores'][0].unsqueeze(0),
    'descriptors0': pred1['descriptors'][0].unsqueeze(0),
    'keypoints1': pred2['keypoints'][0].unsqueeze(0),
    'scores1': pred2['scores'][0].unsqueeze(0),
    'descriptors1': pred2['descriptors'][0].unsqueeze(0),
}

with torch.no_grad():
    pred = superglue(data)

matches = pred['matches0'][0].cpu().numpy()
valid = matches > -1

kpts0 = pred1['keypoints'][0].cpu().numpy()
kpts1 = pred2['keypoints'][0].cpu().numpy()
matched_kpts0 = kpts0[valid]
matched_kpts1 = kpts1[matches[valid]]

print(f"匹配到 {len(matched_kpts0)} 对关键点")


# ==========================
# 5. 可视化匹配结果
# ==========================
def draw_matches(img1, img2, kpts0, kpts1):
    h1, w1 = img1.shape
    h2, w2 = img2.shape
    vis = np.zeros((max(h1, h2), w1 + w2), dtype=np.uint8)
    vis[:h1, :w1] = img1
    vis[:h2, w1:] = img2

    vis = cv2.cvtColor(vis, cv2.COLOR_GRAY2BGR)

    for (x0, y0), (x1, y1) in zip(kpts0, kpts1):
        color = tuple(np.random.randint(0, 255, 3).tolist())
        cv2.circle(vis, (int(x0), int(y0)), 3, color, -1)
        cv2.circle(vis, (int(x1 + w1), int(y1)), 3, color, -1)
        cv2.line(vis, (int(x0), int(y0)), (int(x1 + w1), int(y1)), color, 1)

    return vis


vis = draw_matches(img1, img2, matched_kpts0, matched_kpts1)
cv2.imwrite("result_matches.jpg", vis)
cv2.imshow("SuperGlue Matches", vis)
cv2.waitKey(0)
cv2.destroyAllWindows()
