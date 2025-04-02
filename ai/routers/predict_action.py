from fastapi import APIRouter, HTTPException
from utils.rag_utils import predict_action
from pydantic import BaseModel

class PredictionRequest(BaseModel):
    text: str

router = APIRouter()

@router.post("/prediction")
async def predict(request: PredictionRequest):
    try:
        result = await predict_action(request.text)
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

