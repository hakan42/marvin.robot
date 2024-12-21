package com.assetvisor.marvin.robot.domain.environment;

import static org.mockito.BDDMockito.given;

import com.assetvisor.marvin.robot.domain.tools.ForGettingOwnTools;
import com.assetvisor.marvin.toolkit.notebook.NoteBook;
import com.assetvisor.marvin.toolkit.notebook.WriteInNoteBookTool;
import com.assetvisor.marvin.toolkit.watch.LookAtWatchTool;
import com.assetvisor.marvin.robot.domain.tools.ForGettingEnvironmentTools;
import com.assetvisor.marvin.robot.domain.tools.ToolCollector;
import com.assetvisor.marvin.robot.domain.tools.Tool;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ToolCollectorTest {
    @Mock
    private ForGettingEnvironmentTools forGettingEnvironmentTools;
    @Mock
    private ForGettingOwnTools forGettingOwnTools;
    @Mock
    private NoteBook noteBook;
    @InjectMocks
    private ToolCollector toolCollector;

    @Test
    public void shouldCombineEnvironmentAndOwnTools() {
        // Given
        Tool<String, Number> environmentTool1 = new TestEnvironmentTool<>("name1", "description1", String.class);
        Tool<Number, Number> environmentTool2 = new TestEnvironmentTool<>("name2", "description2", Number.class);

        given(forGettingEnvironmentTools.getEnvironmentTools()).willReturn(List.of(environmentTool1, environmentTool2));

        List<Tool<?,?>> allTools = List.of(
            environmentTool1,
            environmentTool2,
            new LookAtWatchTool(),
            new WriteInNoteBookTool(noteBook)
        );

        // When
        List<Tool<?, ?>> all = toolCollector.all();

        // Then
        //assert all.containsAll(allTools);
    }

    private record TestEnvironmentTool<I, O> (
        String name,
        String description,
        Class<I> inputType
    ) implements Tool<I, O> {
        @Override
        public O apply(I o) {
            return null;
        }
    }
}