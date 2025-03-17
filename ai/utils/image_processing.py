import cv2
import numpy as np
import base64
import logging

logger = logging.getLogger("logger")

def decode_image(image_base64: str) -> np.ndarray | None:
    """Base64로 인코딩된 문자열을 이미지로 변환"""
    try:
        image_data = base64.b64decode(image_base64)
        image_array = np.frombuffer(image_data, np.uint8)
        return cv2.imdecode(image_array, cv2.IMREAD_COLOR)
    except Exception as e:
        logger.error(f"이미지 디코딩 오류: {e}")
        return None
