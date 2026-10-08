package chap08.downcasting;

class Animal {
    public void move() {
        System.out.println("동물이 움직입니다.");
    }
}

class Huamn extends Animal {
    public void move() {
        System.out.println("사람이 두발로 걷습니다.");
    }
    public void reBook() {
        System.out.println("사람이 책을 읽습니다.");
    }
}

class Tiger extends Animal {
    public void move() {
        System.out.println("호랑이는 네발로 뜁니다.");
    }
    public void hunting(){
        System.out.println("호랑이가 사냥을 합니다.");
    }
}

class Eegle extends Animal {
    public void move() {
        System.out.println("독수리가 하늘을 날아갑니다.");
    }
    public void flying(){
        System.out.println("하늘을 날아갑니다.");
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

       /* Huamn huamn = (Huamn) animal;
        huamn.reBook(); ==> 눈으로는 보이지만 자바가 다운됨.*/

        //instanceof은 다운캐스팅에 사용함
        if (animal instanceof Huamn){
            Huamn human = (Huamn) animal;
            human.reBook();
        } else if (animal instanceof Tiger){
            Tiger tiger = (Tiger) animal;
            tiger.hunting();
        } else if (animal instanceof Eegle) {
            Eegle eegle = (Eegle) animal;
            eegle.flying();;
        } else {
            System.out.println("기능이 없습니다.");
        }
    }

}
