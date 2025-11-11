import numpy as np
import cv2
import torch
from models.superpoint import SuperPoint
from models.superglue import SuperGlue

device = 'cpu'
superpoint = SuperPoint({'max_keypoints': 1024}).eval().to(device)
superglue = SuperGlue({'weights': 'indoor'}).eval().to(device)


def load_gray(path):
    img = cv2.imread(path, cv2.IMREAD_GRAYSCALE)
    if img is None:
        raise FileNotFoundError(path)
    tensor = torch.from_numpy(img / 255.).float()[None, None].to(device)
    return img, tensor


def main():
    template_img, template_tensor = load_gray('template.png')
    scene_img, scene_tensor = load_gray('test_micro_1.png')

    pred_t = superpoint({'image': template_tensor})
    pred_s = superpoint({'image': scene_tensor})

    data = {
        'keypoints0': pred_t['keypoints'],
        'scores0': pred_t['scores'],
        'descriptors0': pred_t['descriptors'],
        'keypoints1': pred_s['keypoints'],
        'scores1': pred_s['scores'],
        'descriptors1': pred_s['descriptors'],
    }

    matches = superglue(data)
    print(matches['matching_socres0'])


if __name__ == "__main__":
    main()
