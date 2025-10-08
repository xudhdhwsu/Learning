- 基态 (S₀) 停留时间 t₀
- 激发态 (S₁) 停留时间 t₁
- 三重态 (T₁) 停留时间 t2


$$
\begin{align}
& f(t_0) = F \sigma e ^{-F \sigma t_0}
\\
& f(t_1) = \frac{1} {\tau _1} e ^{- t_1 / \tau _1}
\\
& f(t_2) = \frac{1}{\tau _2} e ^{- t_2 / \tau _2}
\\
& F(t) = ∫_0 ^t f(τ) dτ = ∫_0 ^t \gamma e^{(-\gamma τ)} dτ = 1 - e^{(- \gamma t)}
\\
& t = -\frac{\ln( 1 - F(t) )}{\gamma} \quad \quad 0 \leq F(t) \leq 1  \quad \quad 0 \leq 1 - F(t) \leq 1
\\
& f(t) = \int _0 ^t F \sigma e ^{-F \sigma t'}  \frac{1} {\tau _1} e ^{- (t_1 - t')/ \tau _1} dt' = \frac{F \sigma }{F \sigma \tau_1 - 1}[e ^{- t / \tau _1} - e ^{-F \sigma t}]
\end{align}
$$