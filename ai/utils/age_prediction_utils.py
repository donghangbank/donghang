import cv2
import torch
from PIL import Image
from transformers import ViTFeatureExtractor, ViTForImageClassification
from pathlib import Path


BASE_DIR = Path(__file__).resolve().parent.parent
MODEL_DIR = BASE_DIR / "models/age_prediction"
model_path = str(MODEL_DIR)

model = ViTForImageClassification.from_pretrained(model_path)
feature_extractor = ViTFeatureExtractor.from_pretrained(model_path)


def predict_age(image: Image) -> int:
    """얼굴 이미지로 연령대 예측"""
    im = Image.fromarray(cv2.cvtColor(image, cv2.COLOR_BGR2RGB))
    inputs = feature_extractor(im, return_tensors="pt")
    with torch.no_grad():
        output = model(**inputs)
    preds = output.logits.softmax(dim=1).argmax(dim=1).item()
    return preds
