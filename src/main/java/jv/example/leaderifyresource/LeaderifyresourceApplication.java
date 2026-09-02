package jv.example.leaderifyresource;

import jakarta.transaction.Transactional;
import jv.example.leaderifyresource.model.*;
import jv.example.leaderifyresource.repository.*;
import jv.example.leaderifyresource.utils.Role;
import jv.example.leaderifyresource.utils.Sort;
import jv.example.leaderifyresource.utils.Visibility;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.List;

@SpringBootApplication
public class LeaderifyresourceApplication {

	static void main(String[] args) {
		SpringApplication.run(LeaderifyresourceApplication.class, args);
	}

	@Bean
	@Transactional
	@Profile("!prod")
    CommandLineRunner seedDatabase(
			UserRepository userRepository,
			LeaderboardRepository leaderboardRepository,
			ValueRepository valueRepository
	) {
		return args -> {
			User user = new User(
					"Jeffrey",
					"{noop}123456",
					"Jeffrey@email.com"
			);
			Leaderboard leaderboard = new Leaderboard();
			leaderboard.setOwner(user);
			leaderboard.setName("Brasileirão");

			UserLeaderboard userLeaderboard = new UserLeaderboard();
			userLeaderboard.setUser(user);
			userLeaderboard.setRole(Role.ADMIN);

			View view = new View();
			view.setName("Base View");
			view.setBase(true);
			view.setUserLeaderboard(userLeaderboard);

			Entry flamengo = new Entry();
			flamengo.setName("Flamengo");

			Entry coritiba = new Entry();
			coritiba.setName("Coritiba");

			Entry internacional = new Entry();
			internacional.setName("Internacional");

			Entry gremio = new Entry();
			gremio.setName("Gremio");

			Metric points = new Metric();
			points.setName("Points");

			Metric goalsScored = new Metric();
			goalsScored.setName("Goals Scored");

			ViewMetricConfig pointsConfig = new ViewMetricConfig();
			pointsConfig.setMetric(points);
			pointsConfig.setView(view);
			pointsConfig.setPosition(0);
			pointsConfig.setSort(Sort.ASC);
			pointsConfig.setVisibility(Visibility.SHOW);


			ViewMetricConfig goalsConfig = new ViewMetricConfig();
			goalsConfig.setMetric(goalsScored);
			goalsConfig.setView(view);
			goalsConfig.setPosition(1);
			goalsConfig.setSort(Sort.ASC);
			goalsConfig.setVisibility(Visibility.SHOW);

			view.addViewMetricConfig(pointsConfig);
			view.addViewMetricConfig(goalsConfig);

			leaderboard.addUserLeaderboard(userLeaderboard);userLeaderboard.addView(view);

			leaderboard.addMetric(points);
			leaderboard.addMetric(goalsScored);

			leaderboard.addEntry(flamengo);
			leaderboard.addEntry(coritiba);
			leaderboard.addEntry(internacional);
			leaderboard.addEntry(gremio);

			userRepository.save(user);

			leaderboardRepository.saveAndFlush(leaderboard);

			Value fPoints = new Value();
			fPoints.setMetric(points);
			fPoints.setEntry(flamengo);
			fPoints.setValue(76D);

			Value fGoalsScored = new Value();
			fGoalsScored.setMetric(goalsScored);
			fGoalsScored.setEntry(flamengo);
			fGoalsScored.setValue(88D);

			Value cPoints = new Value();
			cPoints.setMetric(points);
			cPoints.setEntry(coritiba);
			cPoints.setValue(55D);

			Value cGoalsScored = new Value();
			cGoalsScored.setMetric(goalsScored);
			cGoalsScored.setEntry(coritiba);
			cGoalsScored.setValue(52D);

			Value iPoints = new Value();
			iPoints.setMetric(points);
			iPoints.setEntry(internacional);
			iPoints.setValue(15D);

			Value iGoalsScored = new Value();
			iGoalsScored.setMetric(goalsScored);
			iGoalsScored.setEntry(internacional);
			iGoalsScored.setValue(15D);

			Value gPoints = new Value();
			gPoints.setMetric(points);
			gPoints.setEntry(gremio);
			gPoints.setValue(15D);

			Value gGoalsScored = new Value();
			gGoalsScored.setMetric(goalsScored);
			gGoalsScored.setEntry(gremio);
			gGoalsScored.setValue(16D);

			valueRepository.saveAll(
					List.of(
							fPoints,
							fGoalsScored,
							cPoints,
							cGoalsScored,
							iPoints,
							iGoalsScored,
							gPoints,
							gGoalsScored
					)
			);

			valueRepository.flush();
		};
	}
}
