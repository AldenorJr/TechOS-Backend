package br.com.tech.os.ostech;

import br.com.tech.os.ostech.model.Client;
import br.com.tech.os.ostech.model.Contact;
import br.com.tech.os.ostech.model.dto.userDTO.UserCreateDTO;
import br.com.tech.os.ostech.model.dto.userDTO.UserUpdateDTO;

public class TestDummies {

    public static UserCreateDTO createUserCreateDTO() {
        return new UserCreateDTO("John Doe", "teste@gmail.com", "123456");
    }

    public static UserUpdateDTO createUserUpdateDTO() {
        return new UserUpdateDTO("Maria Gomes", "teste-update@gmail.com", "123456");
    }

    public static Client buildClient(String id, String name, String cpf, String email, String phone) {
        Client client = new Client();
        client.setId(id);
        client.setName(name);
        client.setCpf(cpf);
        Contact contact = new Contact();
        contact.setEmail(email);
        contact.setPhone(phone);
        client.setContact(contact);
        return client;
    }

}
