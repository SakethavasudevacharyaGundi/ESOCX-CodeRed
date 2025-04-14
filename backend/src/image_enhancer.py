import cv2
import numpy as np
from PIL import Image
import io

class ImageEnhancer:
    @staticmethod
    def enhance_image(image_data):
        """
        Enhance an image using OpenCV.

        Args:
            image_data (bytes): Raw image data

        Returns:
            bytes: Enhanced image data
        """
        # Convert bytes to numpy array
        nparr = np.frombuffer(image_data, np.uint8)
        img = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

        # Convert to grayscale for some operations
        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

        # Apply adaptive thresholding
        thresh = cv2.adaptiveThreshold(
            gray, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C,
            cv2.THRESH_BINARY, 11, 2
        )

        # Apply denoising
        denoised = cv2.fastNlMeansDenoisingColored(img, None, 10, 10, 7, 21)

        # Enhance contrast
        lab = cv2.cvtColor(denoised, cv2.COLOR_BGR2LAB)
        l, a, b = cv2.split(lab)
        clahe = cv2.createCLAHE(clipLimit=3.0, tileGridSize=(8,8))
        cl = clahe.apply(l)
        enhanced = cv2.merge((cl,a,b))
        enhanced = cv2.cvtColor(enhanced, cv2.COLOR_LAB2BGR)

        # Convert back to bytes
        _, buffer = cv2.imencode('.png', enhanced)
        return buffer.tobytes()

    @staticmethod
    def enhance_pdf_page(pdf_page):
        """
        Enhance a PDF page image.

        Args:
            pdf_page (PIL.Image): PDF page as PIL Image

        Returns:
            PIL.Image: Enhanced image
        """
        # Convert PIL Image to OpenCV format
        img = cv2.cvtColor(np.array(pdf_page), cv2.COLOR_RGB2BGR)

        # Apply enhancement
        enhanced = ImageEnhancer.enhance_image(
            cv2.imencode('.png', img)[1].tobytes()
        )

        # Convert back to PIL Image
        return Image.open(io.BytesIO(enhanced))