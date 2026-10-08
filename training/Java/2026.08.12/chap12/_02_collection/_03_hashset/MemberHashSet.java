package chap12._02_collection._03_hashset;

import chap12._02_collection._04_treeset.Member;

import java.util.HashSet;
import java.util.Iterator;

public class MemberHashSet {
    private HashSet<Member> hashSet;
    public MemberHashSet() { hashSet = new HashSet<>();}
    public void addMember(Member member) {
        hashSet.add(member);
    }

    public boolean removeMember(int memberId) {
        return false;
    }
    public void showAllMember(){
        for (Member member: hashSet) {
            System.out.println(member);
        }
        System.out.println();
    }
}
