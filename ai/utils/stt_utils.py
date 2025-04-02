import logging
import io

logger = logging.getLogger("logger")


def stt(audio_data: bytes, model):
    """STT 변환"""
    try:
        audio_stream = io.BytesIO(audio_data)
        segments, _ = model.transcribe(
            audio_stream, batch_size=16, language="ko", vad_filter=True
        )
        return " ".join([segment.text for segment in segments])
    except Exception as e:
        logger.error(f"STT 변환 오류: {e}")
        return ""


def stt_from_path(filepath, model):
    """오디오 파일 경로를 받아서 STT 변환"""
    try:
        segments, _ = model.transcribe(
            filepath, batch_size=16, language="ko", vad_filter=True
        )
        return " ".join([segment.text for segment in segments])
    except Exception as e:
        raise RuntimeError(f"STT 변환 오류: {e}")
