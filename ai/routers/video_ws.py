from fastapi import APIRouter, WebSocket, WebSocketDisconnect
import json
import logging
from utils.image_processing import decode_image
from utils.age_prediction_utils import predict_age
from utils.calling_detection_utils import detect_calling
from utils.face_recognition_utils import face_recognition
logger = logging.getLogger("logger")

router = APIRouter()


@router.websocket("/ws/video")
async def websocket_endpoint(websocket: WebSocket):
    await websocket.accept()
    logger.info("웹소켓 연결")
    try:
        while True:
            data = await websocket.receive_text()
            message = json.loads(data)

            if 'image' not in message:
                logger.error("이미지가 전송되지 않았습니다.")
                continue

            img = decode_image(message['image'])
            if img is None:
                logger.error("이미지 디코딩에 실패했습니다.")
                continue

            predicted_age = predict_age(img)+1 if face_recognition(img) else 0
            calling_detection = detect_calling(img)

            result = {"predicted_age": predicted_age,
                      "calling_detection": calling_detection}
            await websocket.send_text(json.dumps(result))
    except WebSocketDisconnect:
        logger.info("웹소켓 연결 종료")
