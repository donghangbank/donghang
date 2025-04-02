from ultralytics import YOLO
from pathlib import Path
from PIL import Image

BASE_DIR = Path(__file__).resolve().parent.parent
MODEL_DIR = BASE_DIR / "models/calling_detection"
yolo_model_path = str(MODEL_DIR / "best.pt")
yolo_model = YOLO(yolo_model_path)
yolo_model.overrides["verbose"] = False
phone_class_index = 0


def detect_calling(image: Image) -> bool:
    """YOLO 모델을 사용하여 통화 탐지"""
    result = yolo_model.predict(source=image, conf=0.6)
    for r in result:
        if r.boxes is not None and len(r.boxes.cls) > 0:
            cls_list = r.boxes.cls.cpu().numpy().tolist()
            if cls_list.count(phone_class_index) > 0:
                return True
    return False
