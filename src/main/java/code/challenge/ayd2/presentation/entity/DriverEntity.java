package code.challenge.ayd2.presentation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "driver")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
public class DriverEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NonNull
    private String name;

    @Column
    @NonNull
    private int age;
}
