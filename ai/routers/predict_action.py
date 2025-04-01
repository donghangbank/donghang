from fastapi import APIRouter, HTTPException
from utils.rag_utils import predict_action

router = APIRouter()

@router.post("/prediction")
async def predict(text: str):
    try:
        result = await predict_action(text)
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
