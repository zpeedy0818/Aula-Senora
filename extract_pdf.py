import fitz
import os

pdf_path = "Documentación/LayOut-componentes-patrones.pdf"
doc = fitz.open(pdf_path)

out_dir = r"C:\Users\Ocada\.gemini\antigravity\brain\459c6108-6acb-417e-8cbb-c701d618afb0\scratch\pdf_images"
os.makedirs(out_dir, exist_ok=True)

print("Extracting images...")
for page_num in range(len(doc)):
    page = doc[page_num]
    image_list = page.get_images()
    for img_index, img in enumerate(image_list, start=1):
        xref = img[0]
        base_image = doc.extract_image(xref)
        image_bytes = base_image["image"]
        image_ext = base_image["ext"]
        image_name = f"page{page_num+1}_img{img_index}.{image_ext}"
        image_path = os.path.join(out_dir, image_name)
        with open(image_path, "wb") as f:
            f.write(image_bytes)
        print(f"Extracted {image_name}")

print("Extracting text with blocks...")
for page_num in range(len(doc)):
    page = doc[page_num]
    text = page.get_text("text")
    if text.strip():
        print(f"--- Page {page_num+1} Text ---")
        print(text.strip())
