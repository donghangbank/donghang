from fastapi import APIRouter, HTTPException, UploadFile, File
from utils.ocr_utils import decode_image, extract_name_id

router = APIRouter()


@router.post("/ocr")
async def ocr(file: UploadFile = File(...)):

    image = await decode_image(file)

    name, id = extract_name_id(image)
    if not name or not id:
        raise HTTPException(status_code=404, detail="이름이나 주민등록번호를 찾을 수 없습니다.")

    return {"name": name, "id": id}
