# StegEmbed
StegEmbed is a steganography program that can embed and extract text in and out of the pixels of an image.

## Features
StegEmbed can both embed text into the pixels of an image, and extract previously embedded text from the pixels of an image.

To embed text, StegEmbed:
- Compresses the text
- Encrypts the text with a given password
- Uses a pseudo random number generator and a given password to get the pixels to embed the text into
- Embeds the compressed and encrypted text into the pixels
- Outputs the embed pixels into a new image

To extract embedded text, StegEmbed:
- Get the pixels from a given image
- Uses a pseudo random number generator and a given password to find the pixels with the text
- Decrypts the text with a given password
- Decompresses the text
- Output the text to a new text file

## Requirements
- A Java JDK distribution must be installed and added to PATH.
- Maven must be installed and added to PATH.

## Compatibility
StegEmbed is compatible with both Windows and Linux.

## Installation
```
# clone StegEmbed
git clone https://github.com/ThatcherDev/StegEmbed.git

# change the working directory to StegEmbed
cd StegEmbed

# build StegEmbed with Maven
mvn clean package
```

Alternatively, you can download the jar from the [release page](https://github.com/ThatcherDev/StegEmbed/releases).

## Usage
```
java -jar stegembed.jar
StegEmbed: A program that embeds and extracts text in and out of the pixels of an image (1.0)

Usage:
	java -jar stegembed.jar   [-h] [-v] [embed -i IMAGE -t TEXTFILE -p PASSWORD]
					[extract -i IMAGE -p PASSWORD]
Arguments:
	-h, --help	Display this message.
	-v, --version	Display current version.
	-t, --text	Specify text file to use for embedding.
	-i, --image	Specify image to use for embedding/extracting.
	-p, --password	Specify password to use for encrypting/decrypting when embedding/extracting.

```

## License
- [MIT](https://choosealicense.com/licenses/mit/)
- Copyright 2019 � ThatcherDev.
