package com.example.Explicacion.service;

import com.example.Explicacion.dto.CreateUserDTO;
import com.example.Explicacion.model.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final Map<Long, User> storage = new LinkedHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    // Constructor para poblar datos iniciales
    public UserService() {
        save(new CreateUserDTO("Ana Perez", "7771112233"));
        save(new CreateUserDTO("Carlos Lopez", "7772223344"));
        save(new CreateUserDTO("Daniela Ruiz", "7773334455"));
    }

    // Crear usuario
    public User save(CreateUserDTO dto) {
        Long id = idGen.getAndIncrement();
        User u = new User(id, dto.getName(), dto.getPhone());
        storage.put(id, u);
        return u;
    }

    // Buscar por ID
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    // Listar con búsqueda y paginación
    public List<User> findAll(int page, int size, String search) {
        List<User> all = new ArrayList<>(storage.values());

        if (search != null && !search.isBlank()) {
            String s = search.toLowerCase();
            all = all.stream()
                    .filter(u ->
                            u.getName().toLowerCase().contains(s) ||
                                    u.getPhone().contains(s)
                    )
                    .collect(Collectors.toList());
        }

        int from = page * size;

        if (from >= all.size()) {
            return Collections.emptyList();
        }

        int to = Math.min(from + size, all.size());

        return all.subList(from, to);
    }

    // Actualizar usuario
    public Optional<User> update(Long id, CreateUserDTO dto) {
        User existing = storage.get(id);

        if (existing == null) return Optional.empty();

        existing.setName(dto.getName());
        existing.setPhone(dto.getPhone());

        return Optional.of(existing);
    }
    // Eliminar usuario
    public boolean delete(Long id) {
        return storage.remove(id) != null;
    }

}
