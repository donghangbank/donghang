from fastapi import UploadFile
import numpy as np
from PIL import Image
import io
import logging

logger = logging.getLogger("logger")

async def decode_image(file: UploadFile) -> np.ndarray:
    """이미지를 OCR에 사용할 수 있도록 변환"""
    try:
        file_bytes = await file.read()
        image = Image.open(io.BytesIO(file_bytes)).convert("RGB")
        image_np = np.array(image)
        return image_np
    
    except Exception as e:
        logger.error(f"이미지 디코딩 오류: {e}")
        return None

