//Image Splitter

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class ImageSplitter {
	public static void main(String[] args) {
		System.out.println("* Image Splitter *");
		if (args.length != 0 && args.length != 3) {
			error("This program's usage is as follows:\n" +
					"java ImageSplitter\n" +
					"java ImageSplitter <input image location> <output resolution option> <output directory location>");
		}
		Scanner input = new Scanner(System.in);
		String inputImageLocation;
		if (args.length == 0) {
			System.out.print("Enter the input image's location: ");
			inputImageLocation = input.nextLine();
		} else {
			inputImageLocation = args[0];
		}
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(inputImageLocation);
		} catch (Exception e) {
			error(inputImageLocation + " is not a valid file.");
		}
		String outputResolutionString;
		if (args.length == 0) {
			System.out.println("Select the output resolution for the subimages:");
			System.out.println("1) 8x8 pixels");
			System.out.println("2) 16x16 pixels");
			System.out.println("3) 32x32 pixels");
			System.out.println("4) 64x64 pixels");
			System.out.print("Output resolution option: ");
			outputResolutionString = input.nextLine();
		} else {
			outputResolutionString = args[1];
		}
		Scanner lineInput = new Scanner(outputResolutionString);
		if (!lineInput.hasNextInt()) {
			error("Invalid output resolution input.");
		}
		int outputResolutionOption = lineInput.nextInt();
		if (outputResolutionOption < 1 || outputResolutionOption > 4) {
			error("Invalid output resolution option.");
		}
		lineInput.close();
		int outputLength = (int)Math.pow(2, outputResolutionOption + 2);
		String outputDirectoryLocation;
		if (args.length == 0) {
			System.out.print("Enter the output directory's location: ");
			outputDirectoryLocation = input.nextLine();
		} else {
			outputDirectoryLocation = args[2];
		}
		File outputDirectory = new File(outputDirectoryLocation);
		if (!outputDirectory.exists() && outputDirectory.mkdirs()) {
			System.out.println(outputDirectoryLocation + " was created.");
		}
		input.close();
		BufferedImage inputImage;
		try {
			inputImage = ImageIO.read(inputStream);
		} catch (Exception e) {
			inputImage = null;
		}
		if (inputImage == null) {
			error(inputImageLocation + " does not contain a readable image.");
		}
		try {
			inputStream.close();
		} catch (Exception e) {
			error("Unable to close inputStream.");
		}
		if (inputImage.getWidth() < 1 || inputImage.getWidth() > 256 ||
				inputImage.getWidth() % outputLength != 0 || inputImage.getHeight() < 1 ||
				inputImage.getHeight() > 256 || inputImage.getHeight() % outputLength != 0) {
			error(inputImageLocation + " has an invalid resolution of " +
					inputImage.getWidth() + "x" + inputImage.getHeight() + " pixels.");
		}
		int subimageIndex = 0;
		int subimagesCreated = 0;
		for (int y = 0; y < inputImage.getHeight(); y += outputLength) {
			for (int x = 0; x < inputImage.getWidth(); x += outputLength) {
				BufferedImage subimage = new BufferedImage(outputLength, outputLength, BufferedImage.TYPE_INT_ARGB);
				for (int subimageY = 0; subimageY < outputLength; subimageY++) {
					for (int subimageX = 0; subimageX < outputLength; subimageX++) {
						subimage.setRGB(subimageX, subimageY, inputImage.getRGB(x + subimageX, y + subimageY));
					}
				}
				String subimageLocation = String.format("%s/Subimage_%04d.png", outputDirectoryLocation, subimageIndex);
				subimageIndex++;
				try {
					ImageIO.write(subimage, "png", new FileOutputStream(subimageLocation));
				} catch (Exception e) {
					System.out.println("Unable to create " + subimageLocation + ".");
					continue;
				}
				subimagesCreated++;
			}
		}
		if (subimagesCreated == 1) {
			System.out.println("Success: " + subimagesCreated + " subimage was created from " +
					inputImageLocation + "!");
		} else if (subimagesCreated > 1) {
			System.out.println("Success: " + subimagesCreated + " subimages were created from " +
					inputImageLocation + "!");
		}
	}
	
	private static void error(String message) {
		System.out.println("Error: " + message);
		System.exit(1);
	}
}
