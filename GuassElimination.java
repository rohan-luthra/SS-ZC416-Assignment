package Mtech;

// Java program to demonstrate working of Guassian Elimination
// method
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class GuassElimination {

    public static int N;

    static class Result {
        private final double time_FE_ms;
        private final double time_BS_ms;

        public Result(double time_FE_ms, double time_BS_ms) {
            this.time_FE_ms = time_FE_ms;
            this.time_BS_ms = time_BS_ms;
        }

    }

    // function to get matrix content
    static Result gaussianElimination(double mat[][]) {

        /* reduction into r.e.f. */
        Long start_FE = new Date().getTime();
        int singular_flag = forwardElim(mat);
        Long end_FE = new Date().getTime();

        /* if matrix is singular */
        if (singular_flag != -1) {
            System.out.println("Singular Matrix.");

            /*
             * if the RHS of equation corresponding to zero row is 0, * system has
             * infinitely many solutions, else inconsistent
             */
            if (mat[singular_flag][N] != 0)
                System.out.print("Inconsistent System.");
            else
                System.out.print("May have infinitely many solutions.");

            return new Result(end_FE - start_FE, 0);
        }

        /*
         * get solution to system and print it using backward substitution
         */
        Long start_BS = new Date().getTime();
        backSub(mat);
        Long end_BS = new Date().getTime();

        return new Result(end_FE - start_FE, end_BS - start_BS);

    }

    // function for elementary operation of swapping two
    // rows
    static void swap_row(double mat[][], int i, int j) {
        // printf("Swapped rows %d and %d\n", i, j);

        for (int k = 0; k <= N; k++) {
            double temp = mat[i][k];
            mat[i][k] = mat[j][k];
            mat[j][k] = temp;
        }
    }

    // function to print matrix content at any stage
    static void print(double mat[][]) {
        for (int i = 0; i < N; i++, System.out.println())
            for (int j = 0; j <= N; j++)
                System.out.print(mat[i][j]);
        System.out.println();
    }

    // function to reduce matrix to r.e.f.
    static int forwardElim(double mat[][]) {
        for (int k = 0; k < N; k++) {

            // Initialize maximum value and index for pivot
            int i_max = k;
            int v_max = (int) mat[i_max][k];

            /* find greater amplitude for pivot if any */
            for (int i = k + 1; i < N; i++)
                if (Math.abs(mat[i][k]) > v_max) {
                    v_max = (int) mat[i][k];
                    i_max = i;
                }

            /*
             * if a prinicipal diagonal element is zero, it denotes that matrix is singular,
             * and will lead to a division-by-zero later.
             */
            if (mat[k][i_max] == 0)
                return k; // Matrix is singular

            /*
             * Swap the greatest value row with current row
             */
            if (i_max != k)
                swap_row(mat, k, i_max);

            for (int i = k + 1; i < N; i++) {

                /*
                 * factor f to set current row kth element to 0, and subsequently remaining kth
                 * column to 0
                 */
                double f = mat[i][k] / mat[k][k];

                /*
                 * subtract fth multiple of corresponding kth row element
                 */
                for (int j = k + 1; j <= N; j++)
                    mat[i][j] -= mat[k][j] * f;

                /*
                 * filling lower triangular matrix with zeros
                 */
                mat[i][k] = 0;
            }

            // print(mat); //for matrix state
        }

        // print(mat); //for matrix state
        return -1;
    }

    // function to calculate the values of the unknowns
    static void backSub(double mat[][]) {
        double x[] = new double[N]; // An array to store solution

        /*
         * Start calculating from last equation up to the first
         */
        for (int i = N - 1; i >= 0; i--) {

            /* start with the RHS of the equation */
            x[i] = mat[i][N];

            /*
             * Initialize j to i+1 since matrix is upper triangular
             */
            for (int j = i + 1; j < N; j++) {

                /*
                 * subtract all the lhs values except the coefficient of the variable whose
                 * value is being calculated
                 */
                x[i] -= mat[i][j] * x[j];
            }

            /*
             * divide the RHS by the coefficient of the unknown being calculated
             */
            x[i] = x[i] / mat[i][i];
        }

        return;
    }

    // create a random matrix
    static void generateRandomMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = Math.random() * 1000;
            }
        }
    }

    // Driver program
    public static void main(String[] args) {

        HashMap<Integer, Result> final_result = new HashMap<>();

        for (int t = 1; t <= 1; t++) {

            /* input matrix */
            for (int i = 1000; i <= 10000; i += 1000) {

                N = i;

                double[][] matrix = new double[i][i + 1];
                generateRandomMatrix(matrix);
                Result result = gaussianElimination(matrix);
                final_result.put(i, result);

                System.out.printf(
                        "For n = %d Avg Time taken  Forward Elimination = %f ms , Backward Substitution = %f ms \n", i,
                        result.time_FE_ms, result.time_BS_ms);

            }

        }

        // System.out.println("\nFor Plotting log(n) - log(FE_time_ms)");
        // for (Map.Entry<Integer, Result> res : final_result.entrySet()) {
        // System.out.printf("x = %f y = %f\n", Math.log10((double) res.getKey()),
        // Math.log10(res.getValue().time_FE_ms));
        // }

        // System.out.println("\nFor Plotting log(n) - log(BS_time_ms)");
        // for (Map.Entry<Integer, Result> res : final_result.entrySet()) {
        // System.out.printf("x = %f y = %f\n", Math.log10((double) res.getKey()),
        // Math.log10(res.getValue().time_BS_ms));
        // }
    }
}