import torch
import numpy as np
from torchvision.models.detection import fasterrcnn_mobilenet_v3_large_fpn
from torchvision.transforms.functional import to_tensor
from PIL import Image


model = fasterrcnn_mobilenet_v3_large_fpn(pretrained=True)
model.eval()

x = [torch.rand(3, 300, 400), torch.rand(3, 500, 400)]
predictions = model(x)

def createImage(width, height, bytedata):
	return Image.frombytes('RGB', (width, height), bytedata, 'raw')

def detect(width, height, bytedata):
	image = createImage(width, height, bytedata)
	image = to_tensor(image)
	predictions = model(image.unsqueeze(0))[0]
	# boxes /= scale
	return predictions['boxes'].detach().numpy(), predictions['scores'].detach().tolist(), predictions['labels'].detach().tolist()

