/*
 * Copyright (c) 2011 - 2013 United ID.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.unitedid.usertool
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter

import javax.imageio.ImageIO
import javax.imageio.stream.ImageOutputStream
import java.awt.image.BufferedImage

class BarCode {
    private static final int WHITE = 0xFFFFFFFF
    private static final int BLUE = 0xFF168DB9

    static def createQrCode(String text, int imageSize, String imageFormat) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter()
        BitMatrix matrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, imageSize, imageSize)
        BufferedImage img = MatrixToImageWriter.toBufferedImage(matrix)
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos)
        ImageIO.write(img, imageFormat, ios)
        def image = baos.toByteArray()
        ios.close()
        baos.close()
        return image
    }

    static def createQrCodeBlue(String text, int imageSize, String imageFormat) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter()
        BitMatrix matrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, imageSize, imageSize)

        // Change default color to United ID's blue/white
        BufferedImage img = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB)
        for (int x = 0; x < imageSize; x++) {
            for (int y = 0; y < imageSize; y++) {
                img.setRGB(x, y, matrix.get(x, y) ? BLUE : WHITE)
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos)
        ImageIO.write(img, imageFormat, ios)
        def image = baos.toByteArray()
        ios.close()
        baos.close()
        return image
    }
}
