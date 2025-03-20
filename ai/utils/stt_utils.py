import logging
import torch

logger = logging.getLogger("logger")

gpu_available = torch.cuda.is_available()
device = "cuda" if gpu_available else "cpu"
