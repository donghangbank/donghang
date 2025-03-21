from fastapi import APIRouter, WebSocket, WebSocketDisconnect
import json
import logging
from utils.rag_utils import *
from utils.stt_utils import *

logger = logging.getLogger("logger")

router = APIRouter()

@router.websocket("/ws/audio")
async def websocket_endpoint(websocket: WebSocket):
    await websocket.accept()
    try:
        while True:
            data = await websocket.receive_bytes()
            logger.info("데이터 받음")
            
            stt_text = await stt(data)
            if not stt_text.strip():
                logger.error("STT 실패")
                await websocket.send_text(json.dumps({
                    "predicted_action": "음성 인식에 실패했습니다."
                }))
                continue
            
            menu_result = await predict_action(stt_text)
            await websocket.send_text(json.dumps(menu_result))
            logger.info("예측 행동 전송")
            
    except WebSocketDisconnect:
        logger.info("클라이언트 연결 종료")
