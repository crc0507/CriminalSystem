package bupt;

import org.jgrapht.*;
import org.jgrapht.alg.interfaces.*;

import java.util.*;

public final class PageRank<V, E>
    implements 
    VertexScoringAlgorithm<V, Double>
{
    /**
     * Default number of maximum iterations.
     */
    public static final int MAX_ITERATIONS_DEFAULT = 100;

    /**
     * Default value for the tolerance. The calculation will stop if the difference of PageRank
     * values between iterations change less than this value.
     */
    public static final double TOLERANCE_DEFAULT = 0.0001;

    /**
     * Damping factor default value.
     */
    public static final double DAMPING_FACTOR_DEFAULT = 0.85d;

    private final Graph<V, E> g;
    private Map<V, Double> scores;

    /**
     * Create and execute an instance of PageRank.
     * 
     * @param g the input graph
     */
    public PageRank(Graph<V, E> g)
    {
        this(g, DAMPING_FACTOR_DEFAULT, MAX_ITERATIONS_DEFAULT, TOLERANCE_DEFAULT);
    }

    /**
     * Create and execute an instance of PageRank.
     * 
     * @param g the input graph
     * @param dampingFactor the damping factor
     */
    public PageRank(Graph<V, E> g, double dampingFactor)
    {
        this(g, dampingFactor, MAX_ITERATIONS_DEFAULT, TOLERANCE_DEFAULT);
    }

    /**
     * Create and execute an instance of PageRank.
     * 
     * @param g the input graph
     * @param dampingFactor the damping factor
     * @param maxIterations the maximum number of iterations to perform
     */
    public PageRank(Graph<V, E> g, double dampingFactor, int maxIterations)
    {
        this(g, dampingFactor, maxIterations, TOLERANCE_DEFAULT);
    }

    /**
     * Create and execute an instance of PageRank.
     * 
     * @param g the input graph
     * @param dampingFactor the damping factor
     * @param maxIterations the maximum number of iterations to perform
     * @param tolerance the calculation will stop if the difference of PageRank values between
     *        iterations change less than this value
     */
    public PageRank(Graph<V, E> g, double dampingFactor, int maxIterations, double tolerance)
    {
        this.g = g;
        this.scores = new HashMap<>();

        if (maxIterations <= 0) {
            throw new IllegalArgumentException("Maximum iterations must be positive");
        }

        if (dampingFactor < 0.0 || dampingFactor > 1.0) {
            throw new IllegalArgumentException("Damping factor not valid");
        }

        if (tolerance <= 0.0) {
            throw new IllegalArgumentException("Tolerance not valid, must be positive");
        }

        run(dampingFactor, maxIterations, tolerance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<V, Double> getScores()
    {
        return Collections.unmodifiableMap(scores);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getVertexScore(V v)
    {
        if (!g.containsVertex(v)) {
            throw new IllegalArgumentException("Cannot return score of unknown vertex");
        }
        return scores.get(v);
    }

    public void run(double dampingFactor, int maxIterations, double tolerance)
    {
        // initialization
        int totalVertices = g.vertexSet().size();
        boolean weighted = g.getType().isWeighted();
        Map<V, Double> weights;
        if (weighted) {
            weights = new HashMap<>(totalVertices);
        } else {
            weights = Collections.emptyMap();
        }

        double initScore = 1.0d / totalVertices;
        for (V v : g.vertexSet()) {
            scores.put(v, initScore);
            if (weighted) {
                double sum = 0;
                for (E e : g.outgoingEdgesOf(v)) {
                    sum += g.getEdgeWeight(e);
                }
                weights.put(v, sum);
            }
        }

        // run PageRank
        Map<V, Double> nextScores = new HashMap<>();
        double maxChange = tolerance;

        while (maxIterations > 0 && maxChange >= tolerance) {
            // compute next iteration scores
            double r = 0d;
            for (V v : g.vertexSet()) {
                if (g.outgoingEdgesOf(v).size() > 0) {
                    r += (1d - dampingFactor) * scores.get(v);
                } else {
                    r += scores.get(v);
                }
            }
            r /= totalVertices;

            maxChange = 0d;
            for (V v : g.vertexSet()) {
                double contribution = 0d;

                if (weighted) {
                    for (E e : g.incomingEdgesOf(v)) {
                        V w = Graphs.getOppositeVertex(g, e, v);
                        contribution +=
                            dampingFactor * scores.get(w) * g.getEdgeWeight(e) / weights.get(w);
                    }
                } else {
                    for (E e : g.incomingEdgesOf(v)) {
                        V w = Graphs.getOppositeVertex(g, e, v);
                        contribution += dampingFactor * scores.get(w) / g.outgoingEdgesOf(w).size();
                    }
                }

                double vOldValue = scores.get(v);
                double vNewValue = r + contribution;
                maxChange = Math.max(maxChange, Math.abs(vNewValue - vOldValue));
                nextScores.put(v, vNewValue);
            }

            // swap scores
            Map<V, Double> tmp = scores;
            scores = nextScores;
            nextScores = tmp;

            // progress
            maxIterations--;
        }

    }

}
