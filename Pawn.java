package org.cis1200.clovecenezlobse;

/**
This class represents a pawn object in the game. Each pawn object has a color (represented by
 a char that is B (blue), R (red), Y (yellow), or G (green). Each pawn also stores whether it is
 the 1st, 2nd, 3rd, or 4th pawn of its color, which allows for differentiated between pawns of the
 same color on the game board. All other functionality is handled in the other classes
 */
public class Pawn {
    char color;
    int identity;
    public Pawn(char color, int identity) {
        this.color = color;
        this.identity = identity;
    }

    /**
     * Getter for color of a pawn
     */
    public char getPawnColor() {
        return this.color;
    }

    /**
     * Getter for identity of the pawn
     */
    public int getPawnIdentity() {
        return this.identity;
    }

    /**
     * Equals method for pawns, allowing for array comparison in the main CloveceNezlobSe class
     */
    /*public boolean equals(Pawn p) {
        return this.color == p.color && this.identity == p.identity;
    } */
    @Override
    public boolean equals(Object obj) {
        Pawn p = (Pawn) obj;
        return this.color == p.color && this.identity == p.identity;
    }
}
