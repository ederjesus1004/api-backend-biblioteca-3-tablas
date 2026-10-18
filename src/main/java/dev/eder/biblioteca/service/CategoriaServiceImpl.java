package dev.eder.biblioteca.service;


import dev.eder.biblioteca.dto.CategoriaRequest;
import dev.eder.biblioteca.dto.CategoriaResponse;
import dev.eder.biblioteca.entidades.Categoria;
import dev.eder.biblioteca.exception.OperacionNoPermitidaException;
import dev.eder.biblioteca.exception.RecursoDuplicadoException;
import dev.eder.biblioteca.exception.RecursoNoEncontradoException;
import dev.eder.biblioteca.mapper.CategoriaMapper;
import dev.eder.biblioteca.repository.CategoriaRepository;
import dev.eder.biblioteca.repository.LibroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService{

    private final CategoriaRepository categoriaRepository;
    private final LibroRepository libroRepository;   // para saber si la categoría tiene hijos


    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAll().stream()
                .map(CategoriaMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponse obtenerPorId(Long id){
        return CategoriaMapper.toResponse(buscarCategoriaId(id));
    }

    @Override
    @Transactional
    public CategoriaResponse crear(CategoriaRequest request){
        // 1. Normalizar
        String nombre = CategoriaMapper.normalizarNombre(request.nombre());

        // 2. Reglas
        if (categoriaRepository.existsByNombreIgnoreCase(nombre)){
            throw new RecursoDuplicadoException(" Ya existe la categoria " + nombre);
        }
        // 3. Mapear
        Categoria categoria = CategoriaMapper.toEntity(request);
        // 4. Guardar y responder
        return CategoriaMapper.toResponse(categoriaRepository.save(categoria));
    }

    @Override
    @Transactional
    public CategoriaResponse actualizar(Long id, CategoriaRequest request){
        // 0. Buscar
        Categoria categoria = buscarCategoriaId(id);
        // 1. Normalizar
        String nombre =CategoriaMapper.normalizarNombre(request.nombre());
        // 2. Reglas: contra OTRA categoría
        if (categoriaRepository.existsByNombreIgnoreCaseAndIdNot(nombre, id)) {
            throw new RecursoDuplicadoException("Ya existe la categoria " + nombre);
        }
        // 3. Mapear sobre la que ya existe
        CategoriaMapper.actualizarEntidad(request, categoria);
        // 4. Guardar y responder
        return CategoriaMapper.toResponse(categoriaRepository.save(categoria));
    }

    @Override
    @Transactional
    public void eliminar(Long id){
        Categoria categoria = buscarCategoriaId(id);

        // un padre con hijos no se borra
        if (libroRepository.existsByCategoriaId(id)){
            throw new OperacionNoPermitidaException(
                    "No se puede eliminar la categoria " + categoria.getNombre() + " porque tiene libros");
        }
        categoriaRepository.delete(categoria);
    }

    private Categoria buscarCategoriaId(Long id){
        return categoriaRepository.findById(id)
                .orElseThrow(()-> new RecursoNoEncontradoException("No existe la categoria con id " + id));
    }

}
