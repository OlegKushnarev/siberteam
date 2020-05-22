package ru.siberteam.word;

public class Word {
    private final String str;

    public Word(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }

    @Override
    public int hashCode() {
        return str.toLowerCase().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof Word) {
            Word anotherWord = (Word) obj;
            return str.equalsIgnoreCase(anotherWord.str);
        }
        return false;
    }
}
