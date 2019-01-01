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
		if (args.length == 0 || args.length == 3) {
			Scanner input = new Scanner(System.in);
			String inputImageLocation;
			if (args.length == 0) {
				System.out.print("Enter the input image's location: ");
				inputImageLocation = input.nextLine();
			} else {
				inputImageLocation = args[0];
			}
			String outputResolutionOption;
			if (args.length == 0) {
				System.out.println("Select the output resolution for the subimages:");
				System.out.println("1) 8x8 pixels");
				System.out.println("2) 16x16 pixels");
				System.out.println("3) 32x32 pixels");
				System.out.println("4) 64x64 pixels");
				System.out.print("Output resolution option: ");
				outputResolutionOption = input.nextLine();
			} else {
				outputResolutionOption = args[1];
			}
			String outputDirectoryLocation;
			if (args.length == 0) {
				System.out.print("Enter the output directory's location: ");
				outputDirectoryLocation = input.nextLine();
			} else {
				outputDirectoryLocation = args[2];
			}
			input.close();
			try {
				FileInputStream inputStream = new FileInputStream(inputImageLocation);
				BufferedImage inputImage = ImageIO.read(inputStream);
				inputStream.close();
				if (inputImage != null) {
					if (isValidInteger(outputResolutionOption) && Integer.parseInt(outputResolutionOption) >= 1 &&
							Integer.parseInt(outputResolutionOption) <= 4) {
						int outputLength = (int)Math.pow(2, Integer.parseInt(outputResolutionOption) + 2);
						if (inputImage.getWidth() >= 1 && inputImage.getWidth() <= 256 &&
								inputImage.getWidth() % outputLength == 0 && inputImage.getHeight() >= 1 &&
								inputImage.getHeight() <= 256 && inputImage.getHeight() % outputLength == 0) {
							File outputDirectory = new File(outputDirectoryLocation);
							if (!outputDirectory.exists() && outputDirectory.mkdir()) {
								System.out.println("(" + outputDirectoryLocation + " was created.)");
							}
							int subimageIndex = 0;
							int subimagesCreated = 0;
							for (int y = 0; y < inputImage.getHeight(); y += outputLength) {
								for (int x = 0; x < inputImage.getWidth(); x += outputLength) {
									BufferedImage subimage = new BufferedImage(outputLength, outputLength,
											BufferedImage.TYPE_INT_ARGB);
									for (int subimageY = 0; subimageY < outputLength; subimageY++) {
										for (int subimageX = 0; subimageX < outputLength; subimageX++) {
											subimage.setRGB(subimageX, subimageY,
													inputImage.getRGB(x + subimageX, y + subimageY));
										}
									}
									String subimageLocation = String.format("%s/Subimage_%04d.png",
											outputDirectoryLocation, subimageIndex);
									try {
										ImageIO.write(subimage, "png", new FileOutputStream(subimageLocation));
										subimagesCreated++;
									} catch (Exception e) {
										System.out.println("Error: Could not create " + subimageLocation + ".");
									}
									subimageIndex++;
								}
							}
							if (subimagesCreated == 1) {
								System.out.println("Success: " + subimagesCreated + " subimage was created from " +
										inputImageLocation + "!");
							} else if (subimagesCreated > 1) {
								System.out.println("Success: " + subimagesCreated + " subimages were created from " +
										inputImageLocation + "!");
							}
						} else {
							System.out.println("Error: " + inputImageLocation + " has an invalid resolution of " +
									inputImage.getWidth() + "x" + inputImage.getHeight() + " pixels.");
						}
					} else {
						System.out.println("Error: Invalid output resolution option.");
					}
				} else {
					System.out.println("Error: " + inputImageLocation + " does not contain a readable image.");
				}
			} catch (Exception e) {
				System.out.println("Error: " + inputImageLocation + " not found.");
			}
		} else {
			System.out.println("This program's usage is as follows:");
			System.out.println("java ImageSplitter");
			System.out.println("java ImageSplitter <input image location> <output resolution option> " +
					"<output directory location>");
		}
	}
	
	private static boolean isValidInteger(String string) {
		if (string.length() >= 2 && string.length() <= 10 && string.charAt(0) == '-') {
			for (int i = 1; i < string.length(); i++) {
				if (string.charAt(i) < '0' || string.charAt(i) > '9') {
					return false;
				}
			}
			return true;
		} else if (string.length() >= 1 && string.length() <= 9) {
			for (int i = 0; i < string.length(); i++) {
				if (string.charAt(i) < '0' || string.charAt(i) > '9') {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
}
