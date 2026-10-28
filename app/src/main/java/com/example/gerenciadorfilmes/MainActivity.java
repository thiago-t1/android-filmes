package com.example.gerenciadorfilmes;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText edtNome;
    private Spinner spnTipo;
    private Spinner spnGenero;
    private EditText edtAno;
    private EditText edtNota;
    private CheckBox chkAssistido;
    private Button btnSalvar;
    private Button btnLimpar;
    private EditText edtBusca;
    private Spinner spnFiltroTipo;
    private RecyclerView recyclerFilmes;

    private FirebaseFirestore db;
    private CollectionReference filmesRef;

    private FilmeAdapter adapter;

    private final List<Filme> listaCompleta = new ArrayList<>();

    private String idEmEdicao = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtNome = findViewById(R.id.edtNome);
        spnTipo = findViewById(R.id.spnTipo);
        spnGenero = findViewById(R.id.spnGenero);
        edtAno = findViewById(R.id.edtAno);
        edtNota = findViewById(R.id.edtNota);
        chkAssistido = findViewById(R.id.chkAssistido);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnLimpar = findViewById(R.id.btnLimpar);
        edtBusca = findViewById(R.id.edtBusca);
        spnFiltroTipo = findViewById(R.id.spnFiltroTipo);
        recyclerFilmes = findViewById(R.id.recyclerFilmes);

        configurarSpinners();

        db = FirebaseFirestore.getInstance();
        filmesRef = db.collection("filmes");

        adapter = new FilmeAdapter(this::preencherFormularioParaEdicao, this::confirmarExclusao);
        recyclerFilmes.setLayoutManager(new LinearLayoutManager(this));
        recyclerFilmes.setAdapter(adapter);

        escutarFilmesEmTempoReal();

        btnSalvar.setOnClickListener(v -> salvarFilme());
        btnLimpar.setOnClickListener(v -> limparFormulario());

        edtBusca.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                aplicarFiltros();
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
            }
        });

        spnFiltroTipo.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                aplicarFiltros();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });
    }

    private void configurarSpinners() {
        ArrayAdapter<CharSequence> tipoAdapter = ArrayAdapter.createFromResource(
                this, R.array.tipos_array, android.R.layout.simple_spinner_item);
        tipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTipo.setAdapter(tipoAdapter);

        ArrayAdapter<CharSequence> generoAdapter = ArrayAdapter.createFromResource(
                this, R.array.generos_array, android.R.layout.simple_spinner_item);
        generoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGenero.setAdapter(generoAdapter);

        ArrayAdapter<CharSequence> filtroTipoAdapter = ArrayAdapter.createFromResource(
                this, R.array.tipos_filtro_array, android.R.layout.simple_spinner_item);
        filtroTipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFiltroTipo.setAdapter(filtroTipoAdapter);
    }

    private void escutarFilmesEmTempoReal() {
        filmesRef.addSnapshotListener((snapshots, erro) -> {
            if (erro != null) {
                Toast.makeText(this, "Erro ao carregar filmes: " + erro.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }

            if (snapshots == null) {
                return;
            }

            listaCompleta.clear();

            for (QueryDocumentSnapshot documento : snapshots) {
                Filme filme = documento.toObject(Filme.class);
                filme.setId(documento.getId());
                listaCompleta.add(filme);
            }

            aplicarFiltros();
        });
    }

    private void aplicarFiltros() {
        String termo = edtBusca.getText().toString().trim().toLowerCase();
        String tipoSelecionado = spnFiltroTipo.getSelectedItem() != null
                ? spnFiltroTipo.getSelectedItem().toString()
                : "Todos";

        List<Filme> filtrados = new ArrayList<>();

        for (Filme filme : listaCompleta) {
            boolean combinaNome = TextUtils.isEmpty(termo)
                    || (filme.getNome() != null && filme.getNome().toLowerCase().contains(termo));

            boolean combinaTipo = "Todos".equals(tipoSelecionado)
                    || tipoSelecionado.equals(filme.getTipo());

            if (combinaNome && combinaTipo) {
                filtrados.add(filme);
            }
        }

        adapter.setFilmes(filtrados);
    }

    private void salvarFilme() {
        String nome = edtNome.getText().toString().trim();
        String tipo = spnTipo.getSelectedItem() != null ? spnTipo.getSelectedItem().toString() : "";
        String genero = spnGenero.getSelectedItem() != null ? spnGenero.getSelectedItem().toString() : "";
        String anoTexto = edtAno.getText().toString().trim();
        String notaTexto = edtNota.getText().toString().trim();
        boolean assistido = chkAssistido.isChecked();

        if (TextUtils.isEmpty(nome)) {
            edtNome.setError("Informe o nome");
            return;
        }

        if (TextUtils.isEmpty(anoTexto)) {
            edtAno.setError("Informe o ano de lançamento");
            return;
        }

        if (TextUtils.isEmpty(notaTexto)) {
            edtNota.setError("Informe a nota pessoal");
            return;
        }

        int ano;
        float nota;

        try {
            ano = Integer.parseInt(anoTexto);
        } catch (NumberFormatException e) {
            edtAno.setError("Ano inválido");
            return;
        }

        try {
            nota = Float.parseFloat(notaTexto);
        } catch (NumberFormatException e) {
            edtNota.setError("Nota inválida");
            return;
        }

        if (nota < 0 || nota > 10) {
            edtNota.setError("A nota deve estar entre 0 e 10");
            return;
        }

        Filme filme = new Filme(nome, tipo, genero, ano, nota, assistido);

        if (idEmEdicao == null) {
            filmesRef.add(filme)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Filme adicionado com sucesso", Toast.LENGTH_SHORT).show();
                        limparFormulario();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Erro ao adicionar filme: " + e.getMessage(), Toast.LENGTH_LONG).show());
        } else {
            filmesRef.document(idEmEdicao).set(filme)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Filme atualizado com sucesso", Toast.LENGTH_SHORT).show();
                        limparFormulario();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Erro ao atualizar filme: " + e.getMessage(), Toast.LENGTH_LONG).show());
        }
    }

    private void preencherFormularioParaEdicao(Filme filme) {
        idEmEdicao = filme.getId();

        edtNome.setText(filme.getNome());
        edtAno.setText(String.valueOf(filme.getAnoLancamento()));
        edtNota.setText(String.valueOf(filme.getNotaPessoal()));
        chkAssistido.setChecked(filme.isAssistido());

        selecionarSpinner(spnTipo, filme.getTipo());
        selecionarSpinner(spnGenero, filme.getGenero());

        btnSalvar.setText("Atualizar");
    }

    private void selecionarSpinner(Spinner spinner, @Nullable String valor) {
        if (valor == null) {
            return;
        }

        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        int posicao = adapter.getPosition(valor);

        if (posicao >= 0) {
            spinner.setSelection(posicao);
        }
    }

    private void confirmarExclusao(Filme filme) {
        new AlertDialog.Builder(this)
                .setTitle("Excluir filme")
                .setMessage("Deseja realmente excluir \"" + filme.getNome() + "\"?")
                .setPositiveButton("Excluir", (dialog, which) -> excluirFilme(filme))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void excluirFilme(Filme filme) {
        filmesRef.document(filme.getId()).delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Filme excluído com sucesso", Toast.LENGTH_SHORT).show();
                    if (filme.getId().equals(idEmEdicao)) {
                        limparFormulario();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Erro ao excluir filme: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void limparFormulario() {
        idEmEdicao = null;
        edtNome.setText("");
        edtAno.setText("");
        edtNota.setText("");
        chkAssistido.setChecked(false);
        spnTipo.setSelection(0);
        spnGenero.setSelection(0);
        btnSalvar.setText("Salvar");
    }
}
