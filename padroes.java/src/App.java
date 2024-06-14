import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Interface para Strategy (Curso)
interface CursoStrategy {
    double calcularXp(int cargaHoraria);
}

// Implementações concretas do Strategy
class CursoJavaStrategy implements CursoStrategy {
    @Override
    public double calcularXp(int cargaHoraria) {
        return cargaHoraria * 12d;
    }
}

class CursoJavaScriptStrategy implements CursoStrategy {
    @Override
    public double calcularXp(int cargaHoraria) {
        return cargaHoraria * 10d;
    }
}

// Singleton Bootcamp usando enum
enum BootcampSingleton {
    INSTANCE;

    private Bootcamp bootcamp;

    BootcampSingleton() {
        bootcamp = new Bootcamp();
        bootcamp.setNome("Bootcamp Java Developer");
        bootcamp.setDescricao("Descrição Bootcamp Java Developer");
    }

    public Bootcamp getBootcamp() {
        return bootcamp;
    }
}

// Classe abstrata Conteudo
abstract class Conteudo {
    protected static final double XP_PADRAO = 10d;

    private String titulo;
    private String descricao;

    public abstract double calcularXp();

    // Getters e Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}

// Classe Curso que herda de Conteudo
class Curso extends Conteudo {
    private CursoStrategy cursoStrategy;
    private int cargaHoraria;

    public Curso(CursoStrategy cursoStrategy) {
        this.cursoStrategy = cursoStrategy;
    }

    @Override
    public double calcularXp() {
        return cursoStrategy.calcularXp(cargaHoraria);
    }

    // Getters e Setters
    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    @Override
    public String toString() {
        return "Curso{" +
                "titulo='" + getTitulo() + '\'' +
                ", descricao='" + getDescricao() + '\'' +
                ", cargaHoraria=" + cargaHoraria +
                '}';
    }
}

// Classe Mentoria que herda de Conteudo
class Mentoria extends Conteudo {
    private LocalDate data;

    public Mentoria() {
    }

    @Override
    public double calcularXp() {
        return XP_PADRAO + 20d;
    }

    // Getters e Setters
    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Mentoria{" +
                "titulo='" + getTitulo() + '\'' +
                ", descricao='" + getDescricao() + '\'' +
                ", data=" + data +
                '}';
    }
}

// Facade para simplificar interações com Bootcamp
class BootcampFacade {
    private Bootcamp bootcamp;

    public BootcampFacade() {
        this.bootcamp = BootcampSingleton.INSTANCE.getBootcamp();
    }

    public void inscreverDev(Dev dev) {
        this.bootcamp.inscreverDev(dev);
    }

    public void adicionarCurso(Curso curso) {
        this.bootcamp.getConteudos().add(curso);
    }

    public void adicionarMentoria(Mentoria mentoria) {
        this.bootcamp.getConteudos().add(mentoria);
    }

    public void progredirDev(Dev dev) {
        dev.progredir();
    }

    public double calcularTotalXpDev(Dev dev) {
        return dev.calcularTotalXp();
    }
}

// Classe Bootcamp
class Bootcamp {
    private String nome;
    private String descricao;
    private final List<Conteudo> conteudos = new ArrayList<>();
    private final Set<Dev> devsInscritos = new HashSet<>();

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Conteudo> getConteudos() {
        return conteudos;
    }

    public Set<Dev> getDevsInscritos() {
        return devsInscritos;
    }

    // Método para inscrever Dev
    public void inscreverDev(Dev dev) {
        this.devsInscritos.add(dev);
        dev.inscreverBootcamp(this);
    }
}

// Classe Dev
class Dev {
    private String nome;
    private Set<Conteudo> conteudosInscritos = new HashSet<>();
    private Set<Conteudo> conteudosConcluidos = new HashSet<>();

    // Método para inscrever Dev em Bootcamp
    public void inscreverBootcamp(Bootcamp bootcamp) {
        this.conteudosInscritos.addAll(bootcamp.getConteudos());
        bootcamp.getDevsInscritos().add(this);
    }

    // Método para progredir nos Conteudos
    public void progredir() {
        if (!conteudosInscritos.isEmpty()) {
            Conteudo conteudo = conteudosInscritos.iterator().next();
            this.conteudosConcluidos.add(conteudo);
            this.conteudosInscritos.remove(conteudo);
        } else {
            System.out.println("Você não está matriculado em nenhum conteúdo.");
        }
    }

    // Método para calcular o XP total
    public double calcularTotalXp() {
        return this.conteudosConcluidos.stream().mapToDouble(Conteudo::calcularXp).sum();
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<Conteudo> getConteudosInscritos() {
        return conteudosInscritos;
    }

    public Set<Conteudo> getConteudosConcluidos() {
        return conteudosConcluidos;
    }

    @Override
    public String toString() {
        return "Dev{" +
                "nome='" + nome + '\'' +
                ", conteudosInscritos=" + conteudosInscritos +
                ", conteudosConcluidos=" + conteudosConcluidos +
                '}';
    }
}

// Classe Principal
public class BootcampApp {
    public static void main(String[] args) {
        // Criando instâncias de Curso usando Strategy
        Curso curso1 = new Curso(new CursoJavaStrategy());
        curso1.setTitulo("Curso Java");
        curso1.setDescricao("Descrição curso Java");
        curso1.setCargaHoraria(8);

        Curso curso2 = new Curso(new CursoJavaScriptStrategy());
        curso2.setTitulo("Curso JavaScript");
        curso2.setDescricao("Descrição curso JavaScript");
        curso2.setCargaHoraria(4);

        // Criando instância de Mentoria
        Mentoria mentoria1 = new Mentoria();
        mentoria1.setTitulo("Mentoria de Java");
        mentoria1.setDescricao("Descrição mentoria Java");
        mentoria1.setData(LocalDate.now());

        // Usando a Facade para interagir com o Bootcamp
        BootcampFacade bootcampFacade = new BootcampFacade();
        bootcampFacade.adicionarCurso(curso1);
        bootcampFacade.adicionarCurso(curso2);
        bootcampFacade.adicionarMentoria(mentoria1);

        // Criando instâncias de Dev e inscrevendo no Bootcamp
        Dev devJoao = new Dev();
        devJoao.setNome("João");
        bootcampFacade.inscreverDev(devJoao);

        Dev devMaria = new Dev();
        devMaria.setNome("Maria");
        bootcampFacade.inscreverDev(devMaria);

        // Progredindo nos conteúdos usando a Facade
        System.out.println("Conteúdos Inscritos João: " + devJoao.getConteudosInscritos());
        bootcampFacade.progredirDev(devJoao);
        bootcampFacade.progredirDev(devJoao);
        System.out.println("Conteúdos Inscritos João: " + devJoao.getConteudosInscritos());
       
