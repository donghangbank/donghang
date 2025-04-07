from fastapi import APIRouter, HTTPException, UploadFile, File
from utils.rag_utils import list_account
from utils.stt_utils import stt
from utils.llm_utils import recommend
from faster_whisper import WhisperModel, BatchedInferencePipeline
import torch

gpu_available = torch.cuda.is_available()
device = "cuda" if gpu_available else "cpu"

model = WhisperModel("small", device=device, compute_type="int8")
batched_model = BatchedInferencePipeline(model=model)


router = APIRouter()


@router.post("/recommend")
async def recommend_acccount(file: UploadFile = File(...)):
    try:
        webm_bytes = await file.read()
        text = stt(webm_bytes, batched_model)
        docs = list_account(text)
        result = recommend(text, docs)
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
