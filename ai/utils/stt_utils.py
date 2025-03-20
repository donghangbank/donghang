import logging
import torch
from faster_whisper import WhisperModel

logger = logging.getLogger("logger")

gpu_available = torch.cuda.is_available()
device = "cuda" if gpu_available else "cpu"

stt_model = WhisperModel(
    "small",
    device=device,
    compute_type="int8"
)
