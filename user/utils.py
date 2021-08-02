
import random
def generate_code():
    source = "0123456789qwertyuioplkjhgfdsazxcvbnmQWERTYUIOPLKJHGFDSAZXCVBNM"
    code = ""
    for i in range(4):
        code += random.choice(source)
    return code
