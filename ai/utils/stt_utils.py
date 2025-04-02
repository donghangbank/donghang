import logging
import torch
from faster_whisper import WhisperModel
import io

logger = logging.getLogger("logger")

gpu_available = torch.cuda.is_available()
device = "cuda" if gpu_available else "cpu"

stt_model = WhisperModel(
    "small",
    device=device,
    compute_type="int8"
)


async def stt(audio_data: bytes):
    """ STT 변환 """
    try:
        audio_stream = io.BytesIO(audio_data)
        segments, _ = stt_model.transcribe(
            audio_stream, language="ko", vad_filter=True)
        return " ".join([segment.text for segment in segments])
    except Exception as e:
        logger.error(f"STT 변환 오류: {e}")
        return ""
