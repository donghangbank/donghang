import cv2
from PIL import Image

classifier = cv2.CascadeClassifier(
    cv2.data.haarcascades + "haarcascade_frontalface_alt.xml")


def face_recognition(image: Image) -> bool:
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    faces = classifier.detectMultiScale(
        gray, scaleFactor=1.1, minNeighbors=4
    )
    return True if len(faces) > 0 else False
