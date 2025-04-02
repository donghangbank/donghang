from fastapi import APIRouter, HTTPException, UploadFile, File, Query
from utils.rag_utils import predict_action
from utils.stt_utils import stt, stt_from_path
from faster_whisper import WhisperModel, BatchedInferencePipeline
import torch

gpu_available = torch.cuda.is_available()
device = "cuda" if gpu_available else "cpu"

model = WhisperModel("small", device=device, compute_type="int8")
batched_model = BatchedInferencePipeline(model=model)


router = APIRouter()


@router.post("/prediction")
async def predict(file: UploadFile = File(...)):
    try:
        webm_bytes = await file.read()
        text = stt(webm_bytes, batched_model)
        result = predict_action(text)
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@router.post("/prediction_path")
async def predict_by_path(filepath: str = Query(...)):
    try:
        text = stt_from_path(filepath, batched_model)
        result = predict_action(text)
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
