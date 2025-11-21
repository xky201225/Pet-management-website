package com.adopt.web;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.SecureRandom;

public class CaptchaServlet extends HttpServlet {
    private static final char[] CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789".toCharArray();
    private static final SecureRandom RND = new SecureRandom();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int w = 140, h = 48;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint bg = new GradientPaint(0, 0, new Color(245, 218, 231), w, h, new Color(233, 255, 203));
        g.setPaint(bg);
        g.fillRect(0, 0, w, h);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) sb.append(CHARS[RND.nextInt(CHARS.length)]);
        String code = sb.toString();

        g.setFont(new Font("Arial", Font.BOLD, 32));
        int x = 12;
        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            double angle = Math.toRadians(RND.nextInt(80) - 20);
            AffineTransform old = g.getTransform();
            g.rotate(angle, x + 12, 28);
            g.setColor(new Color(80 + RND.nextInt(120), 80 + RND.nextInt(120), 80 + RND.nextInt(120)));
            g.drawString(String.valueOf(c), x, 36);
            g.setTransform(old);
            x += 24;
        }

        g.setStroke(new BasicStroke(2f));
        for (int i = 0; i < 8; i++) {
            g.setColor(new Color(120 + RND.nextInt(135), 120 + RND.nextInt(135), 120 + RND.nextInt(135), 120));
            int x1 = RND.nextInt(w), y1 = RND.nextInt(h), x2 = RND.nextInt(w), y2 = RND.nextInt(h);
            g.drawLine(x1, y1, x2, y2);
        }
        for (int i = 0; i < 240; i++) {
            g.setColor(new Color(RND.nextInt(255), RND.nextInt(255), RND.nextInt(255), 60));
            int px = RND.nextInt(w), py = RND.nextInt(h);
            g.fillOval(px, py, 2, 2);
        }
        g.dispose();

        HttpSession s = req.getSession(true);
        s.setAttribute("captchaCode", code);

        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);
        resp.setContentType("image/png");
        ImageIO.write(img, "png", resp.getOutputStream());
    }
}