package com.example.demo.service;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.exif.ExifIFD0Directory;

/** 사진의 회전 정보(EXIF Orientation)를 반영해 이미지를 똑바로 세운다. */
@Component
public class ImageOrienter {

	public record Upright(byte[] bytes, String contentType) {
	}

	public Upright upright(byte[] original, BufferedImage image, String contentType) {
		int orientation = readOrientation(original);
		if (orientation <= 1 || orientation > 8) {
			return new Upright(original, contentType);
		}
		return new Upright(toJpeg(transform(image, orientation)), "image/jpeg");
	}

	private int readOrientation(byte[] bytes) {
		try {
			var metadata = ImageMetadataReader.readMetadata(new ByteArrayInputStream(bytes));
			var directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
			if (directory != null && directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
				return directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
			}
		} catch (Exception e) {
			// 회전 정보가 없거나 읽지 못하면 원본 방향 그대로 쓴다
		}
		return 1;
	}

	private BufferedImage transform(BufferedImage source, int orientation) {
		int w = source.getWidth();
		int h = source.getHeight();
		boolean swap = orientation >= 5;

		AffineTransform t = switch (orientation) {
			case 2 -> new AffineTransform(-1, 0, 0, 1, w, 0);
			case 3 -> new AffineTransform(-1, 0, 0, -1, w, h);
			case 4 -> new AffineTransform(1, 0, 0, -1, 0, h);
			case 5 -> new AffineTransform(0, 1, 1, 0, 0, 0);
			case 6 -> new AffineTransform(0, 1, -1, 0, h, 0);
			case 7 -> new AffineTransform(0, -1, -1, 0, h, w);
			default -> new AffineTransform(0, -1, 1, 0, 0, w); // 8
		};

		BufferedImage result = new BufferedImage(swap ? h : w, swap ? w : h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = result.createGraphics();
		g.drawImage(source, t, null);
		g.dispose();
		return result;
	}

	private byte[] toJpeg(BufferedImage image) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			ImageIO.write(image, "jpg", out);
			return out.toByteArray();
		} catch (IOException e) {
			throw new IllegalStateException("이미지를 변환하지 못했습니다.", e);
		}
	}
}
