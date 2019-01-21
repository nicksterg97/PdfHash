/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Nick Stergiopoulos <your.name at your.org>
 */
import com.itextpdf.text.pdf.PdfTemplate;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class Hash {

    String fileName;
    String pdfName;

    public Hash() {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        fileName = f.getAbsolutePath();
        userInput();
    }
    
   public void userInput()
   {
        JFrame frame = new JFrame("Όνομα PDF");
        pdfName=JOptionPane.showInputDialog(frame, "Πληκτρολογήστε όνομα αρχείου:");
   }

    public byte[] encryptFile() {
        byte[] digest = null;
        try {
            try {
                digest = MessageDigest.getInstance("SHA-512").digest(Files.readAllBytes(Paths.get(fileName)));

             
            } catch (IOException ex) {
                Logger.getLogger(Hash.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Hash.class.getName()).log(Level.SEVERE, null, ex);
        }
        return digest;
    }
    

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public void pdfCreator() {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA, 8);     
            byte[] digest = encryptFile();
            String hashedFile= bytesToHex(digest);
            System.out.println(hashedFile);
            contentStream.beginText();
            contentStream.newLineAtOffset(0,750);

            contentStream.showText(hashedFile);
        
            contentStream.endText();
            contentStream.close();
            pdfName=pdfName+".pdf";
            document.save(pdfName);
            document.close();
        } catch (IOException ex) {
            Logger.getLogger(Hash.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
        Hash h1 = new Hash();
        h1.pdfCreator();
    }

}
