package chap08.polymorphism;

class Animal {
    public void move() {
        System.out.println("동물이 움직입니다.");
    }
}

class Huamn extends Animal {
    public void move() {
        System.out.println("사람이 두발로 걷습니다.");
    }
}

class Tiger extends Animal {
    public void move() {
        System.out.println("호랑이는 네발로 뜁니다.");
    }
}

class Eegle extends Animal {
    public void move() {
        System.out.println("독수리가 하늘을 날아갑니다.");
    }
}

public class AnimalTest {
    public static void main(String[] args) {
        AnimalTest test = new AnimalTest();
        test.moveAnimal(new Huamn());
        test.moveAnimal(new Tiger());
        test.moveAnimal(new Eegle());
    }

    public void moveAnimal(Animal animal) {
        animal.move(); //다양성을 나타냄
        //Animal animal = new Human() ==> 상속 전
        //Animal animal = new Human() ==> (Animal animal) ==> 상속 후
    }
}
