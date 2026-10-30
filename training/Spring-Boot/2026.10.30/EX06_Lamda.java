import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class EX06_Lamda {

	public static void main(String[] args) {
		List<String> list = Arrays.asList("a", "b", "c", "aa" , "aaa", "bb");
		//을 스트림과 람다식을 사용해서  "a" 가 들어간 이름을 추출
		
		Stream<String> stream = list.stream().filter(name -> name.contains("a"));
		stream.forEach(System.out::println);
		
		List<String> names = Arrays.asList("a", "b", "c", "aa" , "aaa", "bb");
		//을 이름을 대문자로 바꾸어서 출력
		Stream<String> stream2 = names.stream().map(s -> s.toUpperCase());
		stream2.forEach(data -> System.out.println(data));
		
		List<String> list3 = Arrays.asList("Java", "Scala", "Groovy", "Python", "Go", "Swift");
		//을 정렬해서 출력
		//위 3개를 풀어 보세요
		Stream<String> stream3 = list3.stream().sorted();
		stream3.forEach(System.out::println);
	}

}
