/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mike.testapp;

import javafx.scene.paint.Color;
import org.controlsfx.glyphfont.Glyph;

/**
 *
 * @author Mike-MSI
 */
public class GlyphUtils {
    
    public static Glyph getGlyph(String name, int size){
        return new Glyph("FontAwesome",name).size(size);
    }
    
    public static Glyph getGlyph(String name, String color){
        return new Glyph("FontAwesome",name).color(Color.valueOf(color));
    }
    
    public static Glyph getGlyph(String name, int size, String color){
        return new Glyph("FontAwesome", name).size(size).color(Color.valueOf(color));
    }
    
    
    
}
