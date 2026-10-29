package io.member.impl;

import io.member.Member;
import io.member.MemberRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ObjectMemberRepository implements MemberRepository {

    private static final String FILE_PATH = "temp/members-obj.dat";

    @Override
    public void add(Member member) {
        List<Member> members = findAll();
        members.add(member);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(members); // 컬렉션을 넣음 (객체 하나 당 하나의 파일에 저장함 - append 옵션이 없는 이유
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Member> findAll() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            Object findObject = ois.readObject();
            return (List<Member>) findObject;
        } catch (FileNotFoundException e) {
            return new ArrayList<>(); // List.of()를 사용하면 안되는 이유: add에서 이 메서드를 불러서 member를 추가하는데 List.of()는 불변 객체여서 데이터를 추가할 수 없음
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
