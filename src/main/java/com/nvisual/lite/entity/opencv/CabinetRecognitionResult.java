package com.nvisual.lite.entity.opencv;


import lombok.Data;
import java.util.List;

@Data
public class CabinetRecognitionResult {
    private boolean success;
    private String message;
    private List<CabinetPosition> positions;

    // 构造方法
    public CabinetRecognitionResult(boolean success, String message, List<CabinetPosition> positions) {
        this.success = success;
        this.message = message;
        this.positions = positions;
    }

    // getter和setter
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<CabinetPosition> getPositions() { return positions; }
    public void setPositions(List<CabinetPosition> positions) { this.positions = positions; }
}