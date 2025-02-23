import React, { useState } from 'react'

const CounterPage = () => {
    const [count, setCount] = useState(0)

    const decrement = () => setCount(count - 1)
    const increment = () => setCount(count + 1)

    return (
        <>
            <div>{count}</div>
            <button onClick={decrement}>-</button>
            <button onClick={increment}>+</button>
        </>
    )
}

export default CounterPage;