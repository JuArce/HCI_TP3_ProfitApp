package ar.edu.itba.hci.profitapp;

import android.app.Application;

import ar.edu.itba.hci.profitapp.repository.AchievementsRepository;
import ar.edu.itba.hci.profitapp.repository.CategoryRepository;
import ar.edu.itba.hci.profitapp.repository.ExerciseRepository;
import ar.edu.itba.hci.profitapp.repository.RoutineRepository;
import ar.edu.itba.hci.profitapp.repository.SportRepository;
import ar.edu.itba.hci.profitapp.repository.UserRepository;

public class App extends Application {

    private AppPreferences preferences;
    private UserRepository userRepository;
    private SportRepository sportRepository;
    private RoutineRepository routineRepository;
    private CategoryRepository categoryRepository;
    private AchievementsRepository achievementsRepository;
    private ExerciseRepository exerciseRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        preferences = new AppPreferences(this);

        userRepository = new UserRepository(this);

        sportRepository = new SportRepository(this);

        routineRepository = new RoutineRepository(this);

        categoryRepository = new CategoryRepository(this);

        achievementsRepository = new AchievementsRepository(this);

        exerciseRepository = new ExerciseRepository(this);
    }

    public AppPreferences getPreferences() {
        return preferences;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public SportRepository getSportRepository() {
        return sportRepository;
    }
    
    public RoutineRepository getRoutineRepository() {
        return routineRepository;
    }

    public CategoryRepository getCategoryRepository() {
        return categoryRepository;
    }

    public AchievementsRepository getAchievementsRepository() {
        return achievementsRepository;
    }

    public ExerciseRepository getExerciseRepository() {
        return exerciseRepository;
    }
}
