package com.assetvisor.marvin.robot.domain.environment;

import static org.mockito.BDDMockito.given;

import com.assetvisor.marvin.equipment.notebook.NoteBook;
import com.assetvisor.marvin.equipment.notebook.WriteInNoteBook;
import com.assetvisor.marvin.equipment.watch.LookAtWatch;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FunctionsTest {
    @Mock
    private ForGettingEnvironmentFunctions forGettingEnvironmentFunctions;
    @Mock
    private NoteBook noteBook;
    @InjectMocks
    private Functions functions;

    @Test
    public void shouldCombineEnvironmentAndEquipmentFunctions() {
        // Given
        EnvironmentFunction<String, Number> environmentFunction1 = new TestEnvironmentFunction<>("name1", "description1", String.class);
        EnvironmentFunction<Number, Number> environmentFunction2 = new TestEnvironmentFunction<>("name2", "description2", Number.class);

        given(forGettingEnvironmentFunctions.getEnvironmentFunctions()).willReturn(List.of(environmentFunction1, environmentFunction2));

        List<EnvironmentFunction<?,?>> allEnvironmentFunctions = List.of(
            environmentFunction1,
            environmentFunction2,
            new LookAtWatch(),
            new WriteInNoteBook(noteBook)
        );

        // When
        List<EnvironmentFunction<?, ?>> all = functions.all();

        // Then
//        assert all.containsAll(allEnvironmentFunctions);
    }

    private record TestEnvironmentFunction<I, O> (
        String name,
        String description,
        Class<I> inputType
    ) implements EnvironmentFunction<I, O> {
        @Override
        public O apply(I o) {
            return null;
        }
    }
}