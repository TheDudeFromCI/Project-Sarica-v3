package me.ci.sarica.agent;

public class Matrix
{
    private final float[] values;
    private final int rows;
    private final int cols;

    public Matrix(int rows, int cols)
    {
        this.rows = rows;
        this.cols = cols;
        values = new float[rows * cols];
    }

	public Matrix(Matrix m)
	{
		rows = m.rows;
		cols = m.cols;
		values = new float[rows * cols];

		for (int i = 0; i < values.length; i++)
			values[i] = m.values[i];
	}

    public int getRows()
    {
        return rows;
    }

    public int getCols()
    {
        return cols;
    }

    public float getValue(int row, int col)
    {
        return values[col * rows + row];
    }

    @Override
    public String toString()
    {
        String s = "";
        s += "[\n";

        for (int r = 0; r < rows; r++)
        {
            s += " [";
            for (int c = 0; c < cols; c++)
            {
                if (c > 0) s += ", ";
                s += getValue(r, c);
            }
            s += "]\n";
        }


        s += "]\n";
        return s;
    }

    public float[] getValues()
    {
        return values;
    }

    public void setValue(int row, int col, float value)
    {
        values[col * rows + row] = value;
    }

    public void setValueByIndex(int index, float value)
    {
        values[index] = value;
    }

    public float getValueByIndex(int index)
    {
        return values[index];
    }

    public int getValueCount()
    {
        return values.length;
    }

    public float meanError()
    {
        double f = 0f;

        for (int i = 0; i < values.length; i++)
            f += Math.abs(values[i]);

        return (float)(f / values.length);
    }

	public Matrix add(Matrix other)
	{
		return add(other, null);
	}

    public Matrix add(Matrix other, Matrix dest)
    {
        if (rows != other.rows || cols != other.cols)
            throw new IllegalArgumentException("Matrix sizes do not match!");

		if (dest == null)
			dest = new Matrix(rows, cols);
		else if (dest.rows != rows || dest.cols != cols)
			throw new IllegalArgumentException("Destination matrix is not the correct size!");

        for (int row = 0; row < rows; row++)
            for (int col = 0; col < cols; col++)
                dest.setValue(row, col, getValue(row, col) + other.getValue(row, col));

        return dest;
    }

    public Matrix transpose()
    {
        Matrix c = new Matrix(getCols(), getRows());

        for (int row = 0; row < c.getRows(); row++)
            for (int col = 0; col < c.getCols(); col++)
                c.setValue(row, col, getValue(col, row));

        return c;
    }

    public Matrix dot(Matrix other)
    {
        if (getCols() != other.getRows())
            throw new IllegalArgumentException("Matrix sizes do not share side dimension!");

        Matrix c = new Matrix(getRows(), other.getCols());

        for (int row = 0; row < c.getRows(); row++)
            for (int col = 0; col < c.getCols(); col++)
            {
                float v = 0f;
                for (int j = 0; j < getCols(); j++)
                    v += getValue(row, j) * other.getValue(j, col);
                c.setValue(row, col, v);
            }

        return c;
    }

    public Matrix sub(Matrix other)
    {
        if (getRows() != other.getRows() || getCols() != other.getCols())
            throw new IllegalArgumentException("Matrix sizes do not match!");

        Matrix c = new Matrix(getRows(), getCols());

        for (int row = 0; row < c.getRows(); row++)
            for (int col = 0; col < c.getCols(); col++)
                c.setValue(row, col, getValue(row, col) - other.getValue(row, col));

        return c;
    }

    public Matrix mul(Matrix other)
    {
        if (getRows() != other.getRows() || getCols() != other.getCols())
            throw new IllegalArgumentException("Matrix sizes do not match!");

        Matrix c = new Matrix(getRows(), getCols());

        for (int row = 0; row < c.getRows(); row++)
            for (int col = 0; col < c.getCols(); col++)
                c.setValue(row, col, getValue(row, col) * other.getValue(row, col));

        return c;
    }

    public Matrix round()
    {
        Matrix c = new Matrix(getRows(), getCols());

        for (int i = 0; i < values.length; i++)
            c.setValueByIndex(i, Math.round(values[i]));

        return c;
    }

    public Matrix mul(float s)
    {
        Matrix c = new Matrix(getRows(), getCols());

        for (int i = 0; i < values.length; i++)
            c.setValueByIndex(i, values[i] * s);

        return c;
    }

    private float sigmoid(float x)
    {
        x = Math.min(15, Math.max(-15, x));
        return 1f / (1f + (float)Math.exp(-x));
    }

    public Matrix sigmoid()
    {
        Matrix c = new Matrix(getRows(), getCols());

        for (int i = 0; i < values.length; i++)
            c.setValueByIndex(i, sigmoid(values[i]));

        return c;
    }

    private float sigmoidDeriv(float x)
    {
        return x * (1f - x);
    }

    public Matrix sigmoidDeriv()
    {
        Matrix c = new Matrix(getRows(), getCols());

        for (int i = 0; i < values.length; i++)
            c.setValueByIndex(i, sigmoidDeriv(values[i]));

        return c;
    }

    private float relu(float x)
    {
        if (x > 10) x = 10;
        if (x < -10) x = -10;
        return x > 0 ? x : x * 0.01f;
    }

    public Matrix relu()
    {
        Matrix c = new Matrix(getRows(), getCols());

        for (int i = 0; i < values.length; i++)
            c.setValueByIndex(i, relu(values[i]));

        return c;
    }

    private float reluDeriv(float x)
    {
        return x > 0 ? 1f : 0.01f;
    }

    public Matrix reluDeriv()
    {
        Matrix c = new Matrix(getRows(), getCols());

        for (int i = 0; i < values.length; i++)
            c.setValueByIndex(i, reluDeriv(values[i]));

        return c;
    }

    public Matrix normalize()
    {
        Matrix m = new Matrix(getRows(), getCols());

        for (int r = 0; r < getRows(); r++)
        {
            float max = 1f;
            for (int c = 0; c < getCols(); c++)
                max = Math.max(max, getValue(r, c));
            for (int c = 0; c < getCols(); c++)
                m.setValue(r, c, getValue(r, c) / max);
        }

        return m;
    }

    public Matrix addRowVector(Matrix other)
    {
        if (getCols() != other.getCols())
            throw new IllegalArgumentException("Matrices must share same number of columns!");
        if (other.getRows() != 1)
            throw new IllegalArgumentException("Vector must have exactly 1 row!");

        Matrix c = new Matrix(getRows(), getCols());

        for (int row = 0; row < getRows(); row++)
            for (int col = 0; col < getCols(); col++)
                c.setValue(row, col, getValue(row, col) + other.getValue(0, col));

        return c;
    }

    public Matrix collapseToColVector()
    {
        Matrix m = new Matrix(1, getCols());

        for (int i = 0; i < getCols(); i++)
        {
            for (int r = 0; r < getRows(); r++)
                m.values[i] += getValue(r, i);
            m.values[i] /= getRows();
        }

        return m;
    }
}
