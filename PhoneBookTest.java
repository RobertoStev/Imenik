package aud9.Imenik;

import java.util.*;

class DuplicateNumberException extends Exception {
    public DuplicateNumberException(String phone) {
        super(String.format("Duplicate number: [%s]", phone));
    }
}

class Contact implements Comparable<Contact> {
    String name;
    String phone;

    public Contact(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public int compareTo(Contact o) {
        int res = this.name.compareTo(o.name);
        if (res == 0) {
            return this.phone.compareTo(o.phone);
        } else {
            return res;
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s", name, phone);
    }
}

class PhoneBook {
    Set<String> allPhoneNumbers; //sprecuva duplikati
    Map<String, Set<Contact>> contactsBySubstring; //gi grupira kontaktite spored subnumber
    Map<String, Set<Contact>> contactsByName; //gi grupura kontaktite spored ime


    public PhoneBook() {
        allPhoneNumbers = new HashSet<>();
        contactsBySubstring = new HashMap<>(); //O(1) za pristap
        contactsByName = new HashMap<>();
    }

    private List<String> getSubstring(String phone) {
        List<String> result = new ArrayList<>();
        for (int len = 3; len <= phone.length(); len++) {
            for (int i = 0; i <= phone.length() - len; i++) {
                result.add(phone.substring(i, i + len)); //site mozni podstringovi od daden telefonski broj
            }
        }
        return result;
    }

    public void addContact(String name, String phone) throws DuplicateNumberException {
        if (allPhoneNumbers.contains(phone)) {
            throw new DuplicateNumberException(phone);
        } else {
            allPhoneNumbers.add(phone);
            Contact c = new Contact(name, phone);
            List<String> subnumbers = getSubstring(phone);
            for (String subnumber : subnumbers) {
                contactsBySubstring.putIfAbsent(subnumber, new TreeSet<>());//stavi TreeSet<>() dokolku ne postoi klucot
                //                  O(1) ?
                contactsBySubstring.get(subnumber).add(c); //dodadi go kontaktot
                //                                O(logn) dodavanje element vo TreeSet
            }

            contactsByName.putIfAbsent(name, new TreeSet<>());
            //                  O(1) ?
            contactsByName.get(name).add(c);
            //                      O(logn) dodavanje element vo TreeSet
        }
    }

    public void contactsByNumber(String number) {
        Set<Contact> contacts = contactsBySubstring.get(number); //O(1) pristap vo HashMap
        if (contacts == null) {
            System.out.println("NOT FOUND");
            return; //da zavrshi metodot
        }
        contacts.forEach(c -> System.out.println(c)); //vrati gi site kontakti koi go sodrzat number
    }

    public void contactsByName(String name) {
        Set<Contact> contacts = contactsByName.get(name); //O(1) pristap vo HashMap
        if (contacts == null) {
            System.out.println("NOT FOUND");
            return; //da zavrshi metodot
        }
        contacts.forEach(c -> System.out.println(c)); //vrati gi site kontakti koi go sodrzat name
    }
}

public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}

// Вашиот код овде


