package edushare.serveredushare.persistence;

import jakarta.persistence.*;

@Entity
@Table(
    name = "Corsi_Seguiti", 
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"Username", "Corso_ID"})
    }
)
public class FollowedCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Username", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "Corso_ID", nullable = false)
    private Course corso;

    public FollowedCourse() {}

    public FollowedCourse(User user, Course corso) {
        this.user = user;
        this.corso = corso;
    }

    // Getter e Setter aggiornati
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Course getCorso() { return corso; }
    public void setCorso(Course corso) { this.corso = corso; }
}