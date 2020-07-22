# StegEmbed
StegEmbed is a stenography program that can embed and extract text into and out of the pixels of an image.

## Features
StegEmbed can both embed text into the pixels of an image, and extract previously embedded text from the pixels of an image.

To embed text, StegEmbed:
- Compresses the text.
- Encrypts the text with a given password.
- Uses a pseudo random number generator and a given password to get the pixels to embed the text into.
- Embeds the compressed and encrypted text into the pixels.
- Outputs the embed pixels to a new image.

To extract embedded text, StegEmbed:
- Get the pixels from a given image.
- Uses a pseudo random number generator and a given password as a seed to get the pixels with embedded the text.
- Decrypts the text with a given password.
- Decompresses the text.
- Output the text to a new text file.

## Demo
<a href="https://asciinema.org/a/0mT0ein8RmMjzounwxLP527qZ" target="_blank"><img src="https://asciinema.org/a/0mT0ein8RmMjzounwxLP527qZ.svg" width="600"/></a>

Original image:

<img src="./demo/original.png" alt="original" width="400"/>

Embedded image:

<img src="./demo/embedded.png" alt="embedded" width="400"/>

## Requirements
- A Java JDK distribution >=8 must be installed and added to PATH.
- The image to be used for embedding/extracting must be a PNG image.

## Compatibility
StegEmbed is compatible with Windows, Mac, and Linux.

## Installation
```
# clone StegEmbed
git clone https://github.com/thatcherclough/StegEmbed.git

# change the working directory to StegEmbed
cd StegEmbed

# build StegEmbed with Maven
# for Windows run
mvnw.cmd clean package

# for Linux run
chmod +x mvnw
./mvnw clean package

# for Mac run
sh mvnw clean package
```

Alternatively, you can download the jar from the [release page](https://github.com/thatcherclough/StegEmbed/releases).

## Usage
```
java -jar stegembed.jar
StegEmbed: A stenography program that can embed and extract text into and out of the pixels of an image (1.0.0)

Usage:
	java -jar stegembed.jar   [-h] [-v] [embed -i IMAGE -t TEXTFILE -p PASSWORD]
				  [extract -i IMAGE -p PASSWORD]
Arguments:
	-h, --help	Display this message.
	-v, --version	Display current version.
	-i, --image	Specify image to use for embedding/extracting.
	-t, --text	Specify text file to use for embedding.
	-p, --password	Specify password to use for encrypting/decrypting when embedding/extracting.
```

## License
- [MIT](https://choosealicense.com/licenses/mit/)
- Copyright 2020 © Thatcher Clough.