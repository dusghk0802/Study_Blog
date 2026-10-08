package chap12._01_generics;

public class GenericPrinter <T extends  Material>{
    private T material;

    public T getMaterial() {
        return material;
    }

    public void setMaterial(T material) {
        this.material = material;
    }

    @Override
    public String toString() {
        return material.toString();
    }
}
